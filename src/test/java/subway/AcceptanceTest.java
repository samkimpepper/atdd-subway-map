package subway;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTest {

    @Autowired
    private DatabaseTruncation databaseTruncation;

    @BeforeEach
    void setup() {
        databaseTruncation.execute();
    }
}