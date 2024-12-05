import java.util.List;

import com.kooozel.Day05;
import com.kooozel.utils.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Day05Test {
    @InjectMocks
    private Day05 day05;

    @Test
    public void testIsValid() {
        //given
        var rules = List.of(new Pair<>(29,13));
        var update = List.of(61,13,29);
        //when
        var result = day05.isValid(rules, update);
        //then
        assertFalse(result);
    }

}
