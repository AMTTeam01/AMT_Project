package ch.heigvd.amt.mvcProject.domain;

public interface IEntity<ENTITY extends IEntity, ID extends Id> {
    ID getId();
    ENTITY deepClone();
}
