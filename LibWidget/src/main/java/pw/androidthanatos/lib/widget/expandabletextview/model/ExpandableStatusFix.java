package pw.androidthanatos.lib.widget.expandabletextview.model;


import pw.androidthanatos.lib.widget.expandabletextview.type.StatusType;

/**
 * @className: ExpandableStatusFix
 * @author: thanatos
 * @des 展开和回收记录 状态查看{@link StatusType}
 */
public interface ExpandableStatusFix {

    /**
     * 设置类型
     * @param status {@link StatusType #STATUS_EXPAND #STATUS_CONTRACT}
     */
    void setStatus(StatusType status);

    /**
     * 获取类型
     * @return {@link StatusType #STATUS_EXPAND #STATUS_CONTRACT}
     */
    StatusType getStatus();
}
