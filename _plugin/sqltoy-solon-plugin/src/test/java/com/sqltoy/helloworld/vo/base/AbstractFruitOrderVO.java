/**
 *@Generated by sagacity-quickvo 4.18
 */
package com.sqltoy.helloworld.vo.base;

import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @project sqltoy-helloworld
 * @version 1.0.0
 * Table: sqltoy_fruit_order,Remark:查询汇总演示-水果订单表
 */
@Entity(tableName="sqltoy_fruit_order",pk_constraint="PRIMARY")
public abstract class AbstractFruitOrderVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4830427150508828666L;

	/**
	 * jdbcType:VARCHAR
	 * 水果名称
	 */
	@Id
	@Column(name="FRUIT_NAME",length=100L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String fruitName;

	/**
	 * jdbcType:INT
	 * 订单月份
	 */
	@Id
	@Column(name="ORDER_MONTH",length=10L,type=java.sql.Types.INTEGER,nullable=false)
	protected Integer orderMonth;

	/**
	 * jdbcType:DECIMAL
	 * 销售数量
	 */
	@Column(name="SALE_COUNT",length=10L,type=java.sql.Types.DECIMAL,nullable=false)
	protected BigDecimal saleCount;

	/**
	 * jdbcType:DECIMAL
	 * 销售单价
	 */
	@Column(name="SALE_PRICE",length=10L,type=java.sql.Types.DECIMAL,nullable=false)
	protected BigDecimal salePrice;

	/**
	 * jdbcType:DECIMAL
	 * 总金额
	 */
	@Column(name="TOTAL_AMT",length=10L,type=java.sql.Types.DECIMAL,nullable=false)
	protected BigDecimal totalAmt;


	/** default constructor */
	public AbstractFruitOrderVO() {
	}

	/** pk constructor */
	public AbstractFruitOrderVO(String fruitName,Integer orderMonth)
	{
		this.fruitName=fruitName;
		this.orderMonth=orderMonth;
	}


	/**
	 *@param fruitName the fruitName to set
	 */
	public void setFruitName(String fruitName) {
		this.fruitName=fruitName;
	}

	/**
	 *@return the FruitName
	 */
	public String getFruitName() {
	    return this.fruitName;
	}

	/**
	 *@param orderMonth the orderMonth to set
	 */
	public void setOrderMonth(Integer orderMonth) {
		this.orderMonth=orderMonth;
	}

	/**
	 *@return the OrderMonth
	 */
	public Integer getOrderMonth() {
	    return this.orderMonth;
	}

	/**
	 *@param saleCount the saleCount to set
	 */
	public void setSaleCount(BigDecimal saleCount) {
		this.saleCount=saleCount;
	}

	/**
	 *@return the SaleCount
	 */
	public BigDecimal getSaleCount() {
	    return this.saleCount;
	}

	/**
	 *@param salePrice the salePrice to set
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice=salePrice;
	}

	/**
	 *@return the SalePrice
	 */
	public BigDecimal getSalePrice() {
	    return this.salePrice;
	}

	/**
	 *@param totalAmt the totalAmt to set
	 */
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt=totalAmt;
	}

	/**
	 *@return the TotalAmt
	 */
	public BigDecimal getTotalAmt() {
	    return this.totalAmt;
	}



	/**
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		StringBuilder columnsBuffer=new StringBuilder();
		columnsBuffer.append("fruitName=").append(getFruitName()).append("\n");
		columnsBuffer.append("orderMonth=").append(getOrderMonth()).append("\n");
		columnsBuffer.append("saleCount=").append(getSaleCount()).append("\n");
		columnsBuffer.append("salePrice=").append(getSalePrice()).append("\n");
		columnsBuffer.append("totalAmt=").append(getTotalAmt()).append("\n");
		return columnsBuffer.toString();
	}

}
