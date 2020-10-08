package ch.heigvd.amt.mvcProject.infrastructure.persistence.memory;

import ch.heigvd.amt.mvcProject.domain.Id;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.exceptions.DataCorruptionException;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.exceptions.IntegrityConstraintViolationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implement in memory of the repository with a hashmap
 */
@ApplicationScoped
@Named("InMemoryUserRepository")
public class InMemoryUserRepository extends InMemoryRepository<User, UserId> implements IUserRepository {

    private Map<UserId, User> database = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        synchronized (user.getUsername()){
            if(findByUsername(user.getUsername()).isPresent()){
                // TODO : gérer l'exception
                // throw new IntegrityConstraintViolationException("Cannot save/update user. The username already exists !");
            }
            super.save(user);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> matchingEntities = findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .collect(Collectors.toList());

        if(matchingEntities.size() < 1)
            return Optional.empty();

        /*
        TODO : gérer l'exception
        if(matchingEntities.size() > 1)
            throw new DataCorruptionException("Two users have the same username !");
         */

        return Optional.of(matchingEntities.get(0).deepClone());
    }
}