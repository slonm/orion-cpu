package ua.orion.core.persistence;

import javax.persistence.*;
import ua.orion.core.annotations.UniqueKey;

/**
 * Сущность-перечислитель. Отличается от NamedEntity уникальностью атрибута name
 * @author sl
 */
@MappedSuperclass
@AttributeOverride(name="name", column=@Column(unique=true))
@UniqueKey("name")
public class AbstractEnumerationEntity<T extends AbstractEnumerationEntity<?>> extends AbstractNamedEntity<T>{

}
