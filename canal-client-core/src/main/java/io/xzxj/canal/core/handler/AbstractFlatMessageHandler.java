package io.xzxj.canal.core.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.FlatMessage;
import io.xzxj.canal.core.listener.EntryListener;
import io.xzxj.canal.core.util.EntryListenerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xzxj
 * @date 2023/3/15 13:42
 */
public abstract class AbstractFlatMessageHandler implements IMessageHandler<FlatMessage> {

    private static final Logger log = LoggerFactory.getLogger(AbstractFlatMessageHandler.class);

    private final List<EntryListener<?>> entryListenerList;
    private final RowDataHandler<List<Map<String, String>>> rowDataHandler;

    public AbstractFlatMessageHandler(List<EntryListener<?>> entryListenerList,
                                      RowDataHandler<List<Map<String, String>>> rowDataHandler) {
        this.entryListenerList = entryListenerList;
        this.rowDataHandler = rowDataHandler;
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        List<Map<String, String>> messageData = flatMessage.getData();
        if (messageData == null || messageData.isEmpty()) {
            return;
        }
        for (int i = 0; i < messageData.size(); i++) {
            CanalEntry.EventType eventType = CanalEntry.EventType.valueOf(flatMessage.getType());
            List<Map<String, String>> maps;
            if (eventType.equals(CanalEntry.EventType.UPDATE)) {
                Map<String, String> map = messageData.get(i);
                Map<String, String> oldMap = flatMessage.getOld().get(i);
                maps = Stream.of(map, oldMap).collect(Collectors.toList());
            } else {
                maps = Stream.of(messageData.get(i)).collect(Collectors.toList());
            }

            String schemaName = flatMessage.getDatabase();
            String tableName = flatMessage.getTable();

            EntryListener<?> entryListener = EntryListenerUtil.findEntryListener(entryListenerList, schemaName, tableName);

            try {
                if (entryListener != null) {
                    rowDataHandler.handleRowData(maps, entryListener, eventType);
                }
            } catch (Exception e) {
                throw new RuntimeException("parse event has an error , data:" + messageData, e);
            }
        }
    }

}
