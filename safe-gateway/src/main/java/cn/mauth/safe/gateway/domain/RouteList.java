package cn.mauth.safe.gateway.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class RouteList implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30,nullable = false,unique = true)
    private String ServerId;
    @Column(nullable = false)
    private Date createTime;
    @Column(nullable = false)
    private Date upTime;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String path;

    private int burstCapacity=10;

    private int replenishRate=20;

    private int prefix=0;

    private int local;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerId() {
        return ServerId;
    }

    public void setServerId(String serverId) {
        ServerId = serverId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getBurstCapacity() {
        return burstCapacity;
    }

    public void setBurstCapacity(int burstCapacity) {
        this.burstCapacity = burstCapacity;
    }

    public int getReplenishRate() {
        return replenishRate;
    }

    public void setReplenishRate(int replenishRate) {
        this.replenishRate = replenishRate;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }
}
