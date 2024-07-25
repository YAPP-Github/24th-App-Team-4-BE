package com.pokit.out.persistence.content.impl

import com.pokit.bookmark.BookmarkFixture
import com.pokit.category.CategoryFixture
import com.pokit.category.model.CategoryImage
import com.pokit.content.ContentFixture
import com.pokit.log.model.LogType
import com.pokit.log.model.UserLog
import com.pokit.out.persistence.bookmark.persist.BookMarkRepository
import com.pokit.out.persistence.bookmark.persist.BookmarkEntity
import com.pokit.out.persistence.category.persist.*
import com.pokit.out.persistence.config.QueryDslConfig
import com.pokit.out.persistence.content.persist.ContentEntity
import com.pokit.out.persistence.content.persist.ContentRepository
import com.pokit.out.persistence.content.persist.toDomain
import com.pokit.out.persistence.log.persist.UserLogEntity
import com.pokit.out.persistence.log.persist.UserLogRepository
import com.pokit.out.persistence.user.persist.UserEntity
import com.pokit.out.persistence.user.persist.UserRepository
import com.pokit.support.TestContainerSupport
import com.pokit.user.UserFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.test.context.ContextConfiguration

@DataJpaTest(includeFilters = [ComponentScan.Filter(Repository::class)])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [TestContainerSupport::class])
@Import(QueryDslConfig::class)
class ContenAdapterTest(
    @Autowired private val contentAdapter: ContenAdapter,
    @Autowired private val contentRepository: ContentRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val categoryRepository: CategoryRepository,
    @Autowired private val categoryImageRepository: CategoryImageRepository,
    @Autowired private val bookMarkRepository: BookMarkRepository,
    @Autowired private val userLogRepository: UserLogRepository

) : BehaviorSpec({
    Given("유저의 컨텐츠 관련 DB 작업을 수행할 때") {
        val user = UserFixture.getUser()
        val savedUser = userRepository.save(UserEntity.of(user))

        val anotherUser = UserFixture.getInvalidUser()
        val savedAnotherUser = userRepository.save(UserEntity.of(anotherUser))

        val image = CategoryImage(1, "www.s3.com")
        val savedImage = categoryImageRepository.save(CategoryImageEntity.of(image))

        val category = CategoryFixture.getCategory(savedUser.id, savedImage.toDomain())
        val savedCategory = categoryRepository.save(CategoryEntity.of(category))

        val content = ContentFixture.getContent(savedCategory.id)
        val savedContent = contentRepository.save(ContentEntity.of(content)).toDomain()

        val content2 = ContentFixture.getContent(savedCategory.id)
        val savedContent2 = contentRepository.save(ContentEntity.of(content2))

        val bookmark = BookmarkFixture.getBookmark(savedContent2.id, savedUser.id)
        bookMarkRepository.save(BookmarkEntity.of(bookmark))

        val userLog = UserLog(savedContent2.id, savedUser.id, LogType.READ)
        userLogRepository.save(UserLogEntity.of(userLog))

        val pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending())

        When("유저 아이디와 컨텐츠 아이디로 조회 시") {
            val findContent = contentAdapter.loadByUserIdAndId(savedUser.id, savedContent.id)!!
            Then("해당 유저가 저장한 컨텐츠가 조회된다.") {
                findContent.id shouldBe savedContent.id
                findContent.categoryId shouldBe savedContent.categoryId
                findContent.data shouldBe savedContent.data
                findContent.title shouldBe savedContent.title
                findContent.memo shouldBe savedContent.memo
                findContent.type shouldBe savedContent.type
            }
        }

        When("자신의 것이 아닌 컨텐츠를 조회 시") {
            val findContent = contentAdapter.loadByUserIdAndId(savedAnotherUser.id, savedContent.id)
            Then("해당 컨텐츠가 조회되지 않는다.") {
                findContent shouldBe null
            }
        }

        When("특정 카테고리 내의 컨텐츠 목록을 조회할 때") {
            When("즐겨찾기한 컨텐츠만 조회하면") {
                val result = contentAdapter.loadAllByUserIdAndContentId(
                    savedUser.id, savedCategory.id, pageRequest, null, true
                )
                Then("두개의 컨텐츠 중 즐겨찾기 한 하나의 컨텐츠만 조회된다.") {
                    result.content.size shouldBe 1
                    val favoriteContent = result.content[0]
                    favoriteContent.id shouldBe savedContent2.id
                }
            }
            When("필터링 조건이 아무것도 없다면") {
                val result = contentAdapter.loadAllByUserIdAndContentId(
                    savedUser.id, savedCategory.id, pageRequest, null, null
                )
                Then("목록이 전체 조회된다.") {
                    result.content.size shouldBe 2
                }
            }
            When("안 읽은 컨텐츠를 조회하면") {
                val result = contentAdapter.loadAllByUserIdAndContentId(
                    savedUser.id, savedCategory.id, pageRequest, false, null
                )
                Then("안 읽은 컨텐츠 하나만 조회된다 (savedContent2)") {
                    result.content.size shouldBe 1
                    result.content[0].id shouldBe savedContent.id
                }
            }
        }
    }
})