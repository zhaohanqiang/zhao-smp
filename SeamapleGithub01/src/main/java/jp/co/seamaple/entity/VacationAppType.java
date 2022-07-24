package jp.co.seamaple.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 休暇申請種別Entity
 */
@Entity
@Data
@Table(name = "m_vacationapp_type")
public class VacationAppType {

    @Id
    @Column(name = "vacationapp_type_id")
    private int vacationAppTypeId; // 休暇申請種別番号

    @Column(name = "vacationapp_type")
    private String vacationAppType; // 休暇申請種別
}
