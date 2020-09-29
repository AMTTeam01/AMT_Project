package ch.heigvd.amt.mvcProject.domain;

public interface IEntity<ENTITY extends IEntity, ID extends Id> {
    /**
     * @return the id of the entity
     */
    ID getId();

    /**
     * @return a deep clone of the entity
     */
    ENTITY deepClone();
}
