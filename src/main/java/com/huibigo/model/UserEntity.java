package com.huibigo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;  //�������� �����ϰ� �ο��Ǵ� id;
	
	@Column(nullable = false)
	private String username; // ���̵�� ����� ��������. �̸����� ���� �׳� ���ڿ��� ���� �ִ�.
	
	private String password; // �н�����.
	
	private String role; // ������� ��. ��: ����, �Ϲݻ����
	
	private String authProvider; //  ���� OAuth���� ����� ���� ���� ������ : github
}
