package uk.joshiejack.shopaholic.shop.comparator;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.MutableComparator;

import java.util.List;
import java.util.Map;

public abstract class AbstractListComparator implements MutableComparator {
    protected List<Comparator> comparators = Lists.newArrayList();

    protected abstract String getTableName();
    protected abstract String getFieldName();

    public void initList(DatabaseLoadedEvent event, String id, Map<String, Comparator> conditions) {
        event.table(getTableName()).where("id="+id).forEach(row -> {
            String other_id = row.get(getFieldName());
            if (!other_id.equals(id)) {
                this.comparators.add(conditions.get(other_id));
            }
        });
    }
}
