package application.service;

import com.yourorganization.domain.User;
import com.yourorganization.out.InMemoryDatabase;
import com.yourorganization.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private InMemoryDatabase mockDatabase; // будем подменять реальную базу

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Инициализируем мок-объекты
        MockitoAnnotations.openMocks(this);

        // Внедряем mock Database в UserService
        userService = new UserService(mockDatabase);
    }

    @Test
    void register_Success() {
        // Допустим, findUserByEmail(...) вернёт null => значит email не занят
        when(mockDatabase.findUserByEmail("test@test.com"))
                .thenReturn(null);

        // Вызываем метод
        User newUser = userService.register("test@test.com", "pass", "Name", User.Role.USER);

        // Проверяем, что вернулся не null и поля совпадают
        assertThat(newUser).isNotNull();
        assertThat(newUser.getEmail()).isEqualTo("test@test.com");
        assertThat(newUser.getPassword()).isEqualTo("pass");
        assertThat(newUser.getName()).isEqualTo("Name");
        assertThat(newUser.getRole()).isEqualTo(User.Role.USER);

        // Убеждаемся, что действительно был вызван saveUser у mockDatabase
        verify(mockDatabase, times(1)).saveUser(any(User.class));
        // А findUserByEmail вызывался 1 раз
        verify(mockDatabase, times(1)).findUserByEmail("test@test.com");
    }

    @Test
    void register_AlreadyExists_ThrowsException() {
        // Если база возвращает уже существующего пользователя, бросаем исключение
        when(mockDatabase.findUserByEmail("test@test.com"))
                .thenReturn(new User("test@test.com", "pass", "OldName", User.Role.USER));

        // Проверяем, что вылетает нужное исключение
        assertThatThrownBy(() -> {
            userService.register("test@test.com", "pass2", "NewName", User.Role.USER);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("уже существует");

        // verify: saveUser(...) вообще не должен был вызываться, так как исключение
        verify(mockDatabase, never()).saveUser(any(User.class));
    }

    @Test
    void login_Success() {
        // При логине mockDatabase должен вернуть юзера с совпадающим паролем
        when(mockDatabase.findUserByEmail("user@domain.com"))
                .thenReturn(new User("user@domain.com", "secret", "Alice", User.Role.USER));

        User user = userService.login("user@domain.com", "secret");

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("user@domain.com");
        assertThat(user.getPassword()).isEqualTo("secret");
        assertThat(user.getName()).isEqualTo("Alice");
    }

    @Test
    void login_WrongPassword_ThrowsException() {
        // При логине пароль не совпал => ошибка
        when(mockDatabase.findUserByEmail("user@domain.com"))
                .thenReturn(new User("user@domain.com", "secret", "Alice", User.Role.USER));

        // Проверка на исключение
        assertThatThrownBy(() -> {
            userService.login("user@domain.com", "badpass");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Неверный email или пароль");
    }
}
