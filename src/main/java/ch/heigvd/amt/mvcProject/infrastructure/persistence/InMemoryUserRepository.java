package ch.heigvd.amt.mvcProject.infrastructure.persistence;

import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryUserRepository implements IUserRepository {

    private Map<UserId, User> database = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        database.put(user.getId(), user);
    }

    @Override
    public void Remove(UserId userId) {
        database.remove(userId);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        User existingUser = database.get(userId);

        if (existingUser == null) {
            return Optional.empty();
        }

        return Optional.of(existingUser);
    }

    @Override
    public Collection<User> findAll() {
        return database.values().stream().map(user -> user.builder().build()).collect(Collectors.toList());
    }

    @Override
    public boolean isUserExist(String username, String password) {
        if (database.containsKey(username)) {
            return database.get(username).equals(password);
        }
        return false;
    }
}
