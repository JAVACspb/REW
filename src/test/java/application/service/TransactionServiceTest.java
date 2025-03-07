package application.service;

import com.yourorganization.domain.Transaction;
import com.yourorganization.out.InMemoryDatabase;
import com.yourorganization.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private InMemoryDatabase mockDatabase;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(mockDatabase);
    }

    @Test
    void createTransaction_Success() {
        // Допустим, сохраним транзакцию
        when(mockDatabase.saveTransaction(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // тогда saveTransaction вернёт тот же объект, что был передан

        Transaction tx = transactionService.createTransaction(
                1L,
                100.0,
                "Food",
                LocalDate.of(2025, 3, 10),
                "Lunch",
                Transaction.TransactionType.EXPENSE
        );

        assertThat(tx).isNotNull();
        assertThat(tx.getUserId()).isEqualTo(1L);
        assertThat(tx.getAmount()).isEqualTo(100.0);
        assertThat(tx.getCategory()).isEqualTo("Food");
        assertThat(tx.getDate()).isEqualTo(LocalDate.of(2025, 3, 10));
        assertThat(tx.getDescription()).isEqualTo("Lunch");
        assertThat(tx.getType()).isEqualTo(Transaction.TransactionType.EXPENSE);

        // Проверяем, что saveTransaction вызывался 1 раз
        verify(mockDatabase, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    void deleteTransaction_Success() {
        // ничего не возвращаем (void)
        doNothing().when(mockDatabase).deleteTransaction(123L);

        transactionService.deleteTransaction(123L);

        // Проверяем, что вызвался именно deleteTransaction(123L)
        verify(mockDatabase, times(1)).deleteTransaction(123L);
    }
}
