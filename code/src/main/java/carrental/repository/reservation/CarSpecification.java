package carrental.repository.reservation;

import carrental.model.reservation.Car;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by Thomas on 23.05.2015.
 */
public class CarSpecification {

    public static Specification<Car> descriptionIsLike(final String description) {
        return new Specification<Car>() {
            @Override
            public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.like(root.get("description"), description);
            }
        };
    }

    public static Specification<Car> colorIsLike(final String color) {
        return new Specification<Car>() {
            @Override
            public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.like(root.get("color"), color);
            }
        };
    }

    public static Specification<Car> licensePlateIs(final String licensePlate) {
        return new Specification<Car>() {
            @Override
            public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.like(root.get("licensePlate"), licensePlate);
            }
        };
    }

    public static Specification<Car> powerIsGreaterThanOrEqual(final Integer minPower) {
        return new Specification<Car>() {
            @Override
            public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.greaterThanOrEqualTo(root.get("power"), minPower);
            }
        };
    }

}
