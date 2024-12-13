import java.util.List;
import java.util.Map;

import com.kooozel.Day05;
import com.kooozel.Day08;
import com.kooozel.utils.Pair;
import com.kooozel.utils.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class Day08Test {
    @InjectMocks
    private Day08 day;

    @Test
    public void testIsValid() {
        //given
        var points = List.of(new Point(9, 9), new Point(5,6));
        var grid = Map.of(new Point(2,2), 'A');
        //when
        var result = day.getPointsOnLine(points, grid);
        //then
        assertNull(result);
    }

}
