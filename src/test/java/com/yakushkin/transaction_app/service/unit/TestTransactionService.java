package com.yakushkin.transaction_app.service.unit;

import com.yakushkin.transaction_app.annotation.UT;
import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.exception.TransactionLimitException;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.repository.TransactionRepository;
import com.yakushkin.transaction_app.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_RICK_BYN;
import static com.yakushkin.transaction_app.util.UtilDataForTest.TRANSACTION_1;
import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_RICK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@UT
class TestTransactionService {
    private static final int TRANSACTION_LIMIT = 100000000;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void saveTransaction() throws TransactionLimitException {
        // Given
        doReturn(TRANSACTION_1).when(transactionRepository).save(TRANSACTION_1);

        // Actions
        final Transaction savedTransaction = transactionService.save(TRANSACTION_1);

        // Expectations
        assertAll(
                () -> assertThat(savedTransaction).isNotNull(),
                () -> assertThat(savedTransaction.getId()).isEqualTo(1),
                () -> assertThat(savedTransaction.getAmount()).isEqualTo(100),
                () -> assertThat(savedTransaction.getAccount()).isEqualTo(ACCOUNT_RICK_BYN),
                () -> assertThat(savedTransaction.getAccount().getUser()).isEqualTo(USER_RICK)
        );
    }

    @Test
    void saveTransactionWithExceededLimit() {
        // Given
        TRANSACTION_1.setAmount(TRANSACTION_LIMIT + 1);

        // Actions
        TransactionLimitException exception = assertThrows(TransactionLimitException.class,
                () -> transactionService.save(TRANSACTION_1));

        // Expectations
        assertAll(
                () -> assertThat(exception).isNotNull(),
                () -> assertInstanceOf(TransactionLimitException.class, exception),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.TRANSACTION_LIMIT_MESSAGE)
        );
    }
}