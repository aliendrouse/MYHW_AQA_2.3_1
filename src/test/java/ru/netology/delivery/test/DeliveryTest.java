package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully schedule and reschedule delivery")
    void shouldScheduleAndRescheduleDelivery() {
        // Генерируем пользовательские данные
        var user = DataGenerator.Registration.generateUser("ru");

        // Первая встреча - через 3 дня
        String firstMeetingDate = DataGenerator.generateDate(3);
        String secondMeetingDate = DataGenerator.generateDate(7);

        // Заполняем форму первый раз
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys("\b");
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();

        // Проверяем успешное планирование
        $("[data-test-id=success-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));

        // Заполняем форму второй раз с новой датой
        $("[data-test-id=date] input").doubleClick().sendKeys("\b");
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("button.button").click();

        // Проверяем уведомление о перепланировании
        $("[data-test-id=replan-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Необходимо подтверждение"));

        // Нажимаем кнопку перепланирования
        $("[data-test-id=replan-notification] button").click();

        // Проверяем успешное перепланирование
        $("[data-test-id=success-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));
    }

    @Test
    @DisplayName("Should schedule delivery with valid data")
    void shouldScheduleDeliveryWithValidData() {
        // Генерируем пользовательские данные
        var user = DataGenerator.Registration.generateUser("ru");
        String meetingDate = DataGenerator.generateDate(5);

        // Заполняем форму
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys("\b");
        $("[data-test-id=date] input").setValue(meetingDate);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();

        // Проверяем успешное планирование
        $("[data-test-id=success-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));
    }
}