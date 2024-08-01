package com.pokit.content.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ContentTest : BehaviorSpec({
    Given("URL이 주어졌을 때") {
        val github = "https://github.com/YAPP-Github/24th-App-Team-4-BE/issues/50"
        val google = "https://www.google.com"
        val youtube = "https://youtu.be/bqSemSf0VqI?si=whk8MyNC_kGYB3Zl"
        val githubData = Content(
            categoryId = 1L,
            type = ContentType.LINK,
            data = github,
            title = "깃허브",
            memo = "test",
            alertYn = "YES"
        )
        val googleData = githubData.copy(data = google)
        val youtubeData = githubData.copy(data = youtube)
        When("도메인을 파싱하면") {
            githubData.parseDomain()
            googleData.parseDomain()
            youtubeData.parseDomain()
            Then("파싱된 도메인이 domain 필드에 저장된다.") {
                githubData.domain shouldBe "github"
                googleData.domain shouldBe "google"
                youtubeData.domain shouldBe "youtube"
            }
        }
    }
})
