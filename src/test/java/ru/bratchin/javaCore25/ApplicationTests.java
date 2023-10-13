package ru.bratchin.javaCore25;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//Тестовый класс добавлен ТОЛЬКО для покрытия вызова main(), не охваченного тестами приложения.

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
		Application.main(new String[]{});
	}

}
