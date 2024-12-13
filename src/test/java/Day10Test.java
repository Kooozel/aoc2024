import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kooozel.Day08;
import com.kooozel.Day10;
import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Day10Test {
    @Mock
    private FileUtils fileUtils;
    @InjectMocks
    private Day10 day;

    @Test
    public void testIsValid() {
        //given
        when(fileUtils.getGridMap(any())).thenCallRealMethod();
        when(fileUtils.readString(any(), any())).thenCallRealMethod();
        var grid = day.parseFile(InputType.TEST);
        var trailHeads = grid.entrySet()
            .stream()
            .filter(entry -> entry.getValue().isTrailHead())
            .collect(Collectors.toSet());
        //when
        trailHeads.forEach(entry -> day.calculateRoutes(entry, grid));
        //then
        var point1 = new Point(0,2);
        var point2 = new Point(0,4);
        var point3 = new Point(2,4);
        var point4 = new Point(4,6);
        var point5 = new Point(5,2);
        var point6 = new Point(5,5);
        var point7 = new Point(6,0);
        var point8 = new Point(6,6);
        var point9 = new Point(7,1);
        assertEquals(5, grid.get(point1).getScore());
        assertEquals(6, grid.get(point2).getScore());
        assertEquals(5, grid.get(point3).getScore());
        assertEquals(3, grid.get(point4).getScore());
        assertEquals(1, grid.get(point5).getScore());
        assertEquals(3, grid.get(point6).getScore());
        assertEquals(5, grid.get(point7).getScore());
        assertEquals(3, grid.get(point8).getScore());
        assertEquals(5, grid.get(point9).getScore());
    }

}
