package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
//@UseStringTemplate3StatementLocator throws an ex
public abstract class UserDao implements AbstractDao {

    public User insert(User user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWitId(user);
        }
        return user;
    }

    @SqlUpdate("INSERT INTO users (full_name, email, flag) VALUES (:fullName, :email, CAST(:flag AS user_flag)) ON CONFLICT (email) DO NOTHING ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean User user);

    void insertWitId(@BindBean User user) {
        insert(Collections.singletonList(user), 1);
    }

    public List<User> insert(
        List<User> users,
        int chunkSize
    ) {
        int nextID = getNextID();
        for (User user : users) {
            user.setId(nextID++);
        }
        setNextID(nextID);

        final int[] ids = insertBatch(users, chunkSize);

//        final ArrayList<String> conflictEmails = new ArrayList<>();
//        for (int i = 0; i < ids.length; i++) {
//            if (ids[i] == 0) {
//                conflictEmails.add(users.get(i).getEmail());
//            }
//        }
//
//        if (!conflictEmails.isEmpty()) {
//            return findByEmail(conflictEmails);
//        }

        final ArrayList<User> conflicts = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == 0) {
                conflicts.add(users.get(i));
            }
        }

        return conflicts;
    }

    @SqlBatch("INSERT INTO users (id, full_name, email, flag) VALUES (:id, :fullName, :email, CAST(:flag AS user_flag)) ON CONFLICT (email) DO NOTHING ")
    abstract int[] insertBatch(
        @BindBean List<User> user,
        @BatchChunkSize int chunkSize
    );

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email LIMIT :it")
    public abstract List<User> getWithLimit(@Bind int limit);

    // doesnt work
//    @SqlQuery("SELECT * FROM users WHERE email in (<emails>) ORDER BY full_name, email LIMIT 501")
//    public abstract List<User> findByEmail(
//        @BindIn("emails") List<String> emails
//    );
//

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT nextval('user_seq')")
    abstract int getNextID();

    @SqlQuery("SELECT setval('user_seq', :it, true)")
    abstract int setNextID(@Bind int id);
}
