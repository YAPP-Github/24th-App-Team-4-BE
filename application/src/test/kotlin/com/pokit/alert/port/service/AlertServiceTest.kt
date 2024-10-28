package com.pokit.alert.port.service

import com.pokit.alert.AlertFixure
import com.pokit.alert.port.out.AlertBatchPort
import com.pokit.alert.port.out.AlertContentPort
import com.pokit.alert.port.out.AlertPort
import com.pokit.alert.port.out.AlertSender
import com.pokit.content.port.out.ContentPort
import com.pokit.user.UserFixture
import com.pokit.user.port.out.FcmTokenPort
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.SliceImpl
import java.time.LocalDateTime
import java.util.function.Supplier

class AlertServiceTest : BehaviorSpec({
    val alertPort = mockk<AlertPort>()
    val now = mockk<Supplier<LocalDateTime>>()
    val alertBatchPort = mockk<AlertBatchPort>()
    val alertSender = mockk<AlertSender>()
    val fcmTokenPort = mockk<FcmTokenPort>()
    val alertContentPort = mockk<AlertContentPort>()
    val contentPort = mockk<ContentPort>()
    val alertService = AlertService(now, alertPort, alertBatchPort, alertSender, fcmTokenPort, alertContentPort, contentPort)

    Given("알림 관련 요청이 들어올 때") {
        val nowTime = LocalDateTime.of(2024, 8, 15, 15, 30)
        val user = UserFixture.getUser()
        val pageable = PageRequest.of(0, 10)
        val alert1 = AlertFixure.getAlert(user.id, "title1", nowTime.minusDays(1))
        val alert2 = AlertFixure.getAlert(user.id, "title2", nowTime)
        val alerts = SliceImpl(mutableListOf(alert1, alert2), pageable, false)
        every { now.get() } returns nowTime
        every { alertPort.loadAllByUserId(user.id, pageable) } returns alerts

        When("조회 시") {
            val result = alertService.getAlerts(user.id, pageable)
            val alertList = result.content
            Then("해당 유저의 알림 목록이 조회된다.") {
                alertList[0].title shouldBe alert1.title
                alertList[1].title shouldBe alert2.title
                alertList[0].createdAt shouldBe "1일 전"
                alertList[1].createdAt shouldBe "오늘"
            }
        }
    }
})
