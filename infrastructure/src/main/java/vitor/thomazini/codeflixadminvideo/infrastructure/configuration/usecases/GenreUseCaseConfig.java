package vitor.thomazini.codeflixadminvideo.infrastructure.configuration.usecases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vitor.thomazini.codeflixadminvideo.application.genre.create.CreateGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.create.DefaultCreateGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.delete.DefaultDeleteGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.delete.DeleteGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.get.GetGenreByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.list.DefaultListGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.list.ListGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.update.DefaultUpdateGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.update.UpdateGenreUseCase;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }
}
