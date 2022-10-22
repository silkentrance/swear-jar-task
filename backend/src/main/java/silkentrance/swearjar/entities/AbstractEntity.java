package silkentrance.swearjar.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class AbstractEntity {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PACKAGE)
    private long id;

    @Override
    public boolean equals(Object other) {
        if (other instanceof AbstractEntity) {
            AbstractEntity abstractEntity = (AbstractEntity) other;
            return this == other
                    // all entities share the same db sequence, so this should be safe
                    || getId() == abstractEntity.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        // all entities share the same db sequence, so this should be safe
        // for newly created instances we return the object identity instead
        return (id == 0L) ? super.hashCode() : Long.hashCode(id);
    }
}
