package application.service;

import com.yourorganization.domain.Goal;
import com.yourorganization.out.InMemoryDatabase;
import com.yourorganization.service.GoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GoalServiceTest {

    @Mock
    private InMemoryDatabase mockDatabase;

    private GoalService goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        goalService = new GoalService(mockDatabase);
    }

    @Test
    void createGoal_Success() {
        // Мокаем сохранение
        when(mockDatabase.saveGoal(any(Goal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Создаём цель
        Goal goal = goalService.createGoal(2L, "Buy a Car", 500000.0);

        assertThat(goal).isNotNull();
        assertThat(goal.getUserId()).isEqualTo(2L);
        assertThat(goal.getTitle()).isEqualTo("Buy a Car");
        assertThat(goal.getTargetAmount()).isEqualTo(500000.0);
        assertThat(goal.getCurrentAmount()).isEqualTo(0.0);

        // Проверяем вызов
        verify(mockDatabase).saveGoal(any(Goal.class));
    }
}
