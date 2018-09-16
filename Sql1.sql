/*
 Get CUST_AVG
 */
create view v1 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, sa.state as STATE, cast(Avg(quant) as bigint) as CUST_AVG
from sales as sa
group by CUSTOMER, STATE, PRODUCT
order by CUSTOMER;


/*
 Get OTHER_STATE_AVG
 */
create view v2 as
select v1.CUSTOMER, v1.PRODUCT, v1.state , cast(Avg(sa.quant) as bigint) as OTHER_STATE_AVG
from sales as sa, v1
where sa.prod = v1.PRODUCT and  sa.cust = v1.CUSTOMER and sa.STATE != v1.STATE
group by v1.CUSTOMER, v1.PRODUCT, v1.state
order by v1.CUSTOMER;


/*
 Get OTHER_PROD_AVG
 */
create view v3 as
select v1.CUSTOMER, v1.PRODUCT, v1.STATE, cast(Avg(sa.quant) as bigint) as OTHER_PROD_AVG
from sales as sa, v1
where sa.STATE = v1.STATE and sa.cust = v1.CUSTOMER and sa.prod != v1.PRODUCT
group by v1.CUSTOMER, v1.PRODUCT, v1.STATE
order by v1.CUSTOMER;

select *
from (v1 natural full outer join v2 natural full outer join v3)
order by customer, product