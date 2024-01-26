package vitor.thomazini.codeflixadminvideo.infrastructure.configuration.usecases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vitor.thomazini.codeflixadminvideo.application.category.create.CreateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.create.DefaultCreateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.delete.DefaultDeleteCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.delete.DeleteCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.get.GetCategoryByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.list.DefaultListCategoriesUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.list.ListCategoriesUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.update.DefaultUpdateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.update.UpdateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;

import java.util.Objects;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}