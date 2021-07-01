package uk.joshiejack.shopaholic.shop.condition;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.shopaholic.api.shop.Condition;

import java.util.List;
import java.util.Map;

public abstract class AbstractListCondition extends Condition {
    protected List<Condition> conditions = Lists.newArrayList();

    protected abstract String getTableName();
    protected abstract String getFieldName();

    public void initList(DatabaseLoadedEvent event, String id, Map<String, Condition> conditions) {
        event.table(getTableName()).where("id="+id).forEach(row -> {
            String other_id = row.get(getFieldName());
            if (!other_id.equals(id)) {
                this.conditions.add(conditions.get(other_id));
            }
        });
    }
}
