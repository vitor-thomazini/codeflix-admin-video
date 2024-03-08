package vitor.thomazini.codeflixadminvideo.application;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public abstract class UseCaseTest implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Mockito.reset(getMocks().toArray());
    }

    protected abstract List<Object> getMocks();

    public List<String> asString(final List<CategoryId> categories) {
        return categories.stream()
                .map(CategoryId::value)
                .toList();
    }
}
