package ru.javaops.masterjava.persist.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public abstract class CityDao implements AbstractDao {
    public int[] insertBatch(List<City> cities, int chunkSize) {
        int nextID = getSeqAndSkip(chunkSize);
        for (City it : cities) {
            it.setId(nextID++);
        }

        return insertBatchInternal(cities, chunkSize);
    }

    @SqlBatch("INSERT INTO cities (key, name) VALUES (:key, :name) ON CONFLICT DO NOTHING")
    abstract int[] insertBatchInternal(@BindBean List<City> cities, @BatchChunkSize int chunkSize);


    @SqlQuery("SELECT nextval('city_seq')")
    abstract int getNextVal();

    @SqlQuery("SELECT setval('city_seq', :it, true)")
    abstract int setNextID(@Bind int id);

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        setNextID(id + step);
        return id;
    }

    @SqlUpdate("TRUNCATE cities")
    @Override
    public abstract void clean();
}
