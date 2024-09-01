import org.example.DataProcessor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

public class DataProcessorTest {
    @Test
    void test_PrepareLines() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Stream<String> result = (Stream<String>) getMethod("prepareLines")
                .invoke(new DataProcessor(), Stream.of("Hallo world", "Nice to be   here"));
        assertThat(result).containsExactly("hallo", "world", "nice", "to", "be", "here");
    }

    @Test
    void test_addAndCount() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DataProcessor dataProcessor = new DataProcessor();
        getMethod("addWordAndCount").invoke(dataProcessor, Stream.of("hallo", "nice", "here", "hallo", "nice", "test", "hallo"));
        assertThat(dataProcessor.getMap()).hasSize(4);
        assertThat(dataProcessor.getMap().get("hallo")).isEqualTo(3);
        assertThat(dataProcessor.getMap().get("nice")).isEqualTo(2);
        assertThat(dataProcessor.getMap().get("here")).isEqualTo(1);
    }

    @Test
    void test_sortAndTriage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DataProcessor dataProcessor = new DataProcessor();
        getMethod("addWordAndCount").invoke(dataProcessor, Stream.of("hallo", "nice", "here", "hallo", "nice", "test", "hallo"));
        Map<String, Integer> result = (Map<String, Integer>) getSortedAndTriageMethod()
                .invoke(dataProcessor, 3);
        assertThat(result).hasSize(3);
    }

    private  Method getMethod(String methodName) throws NoSuchMethodException {
        Method method = DataProcessor.class.getDeclaredMethod(methodName, Stream.class);
        method.setAccessible(true);
        return method;
    }

    private  Method getSortedAndTriageMethod() throws NoSuchMethodException {
        Method method = DataProcessor.class.getDeclaredMethod("sortAndTriage", int.class);
        method.setAccessible(true);
        return method;
    }

}
