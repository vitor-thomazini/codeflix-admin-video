package vitor.thomazini.codeflixadminvideo.infrastructure.category;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryRepository;
import vitor.thomazini.codeflixadminvideo.infrastructure.utils.SpecificationUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static vitor.thomazini.codeflixadminvideo.infrastructure.utils.SpecificationUtils.like;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Category create(final Category category) {
        return this.save(category);
    }

    @Override
    public void deleteById(final CategoryId id) {
        final var idValue = id.value();
        if (this.repository.existsById(idValue)) {
            this.repository.deleteById(idValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryId id) {
        return this.repository.findById(id.value()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category category) {
        return this.save(category);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CategoryId> existsByIds(final Iterable<CategoryId> categoryIds) {
        final var ids = StreamSupport.stream(categoryIds.spliterator(), false)
                .map(CategoryId::value)
                .toList();
        return this.repository.existsByIds(ids).stream()
                .map(CategoryId::from)
                .toList();
    }

    private Category save(Category category) {
        final var entity = CategoryJpaEntity.from(category);
        return this.repository.save(entity).toAggregate();
    }

    private Specification<CategoryJpaEntity> assembleSpecification(final String str) {
        final Specification<CategoryJpaEntity> nameLike = like("name", str);
        final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
        return nameLike.or(descriptionLike);
    }
}