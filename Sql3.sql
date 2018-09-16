create view View_Half as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, sum(quant)/2 as Half_Q
from sales as sa
group by CUSTOMER, PRODUCT
order by CUSTOMER;

create view View_Month as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, sa.month as MONTH
from sales as sa
group by CUSTOMER, PRODUCT, MONTH
order by CUSTOMER;

create view Month_Total as
select vm.CUSTOMER, vm.PRODUCT, vm.MONTH, sum(sa.quant) as MONTH_Total
from sales as sa , View_Month as vm
where vm.CUSTOMER = sa.cust and vm.PRODUCT = sa.prod and sa.month <= vm.MONTH
group by vm.CUSTOMER, vm.PRODUCT, vm.MONTH
order by CUSTOMER;

create view View_Final as 
select mt.CUSTOMER, mt.PRODUCT, mt.MONTH as MONTH
from Month_Total as mt, View_Half as vh
where mt.MONTH_Total >= vh.Half_Q and mt.CUSTOMER = vh.CUSTOMER and mt.PRODUCT = vh.PRODUCT
group by mt.CUSTOMER, mt.PRODUCT, mt.MONTH
order by CUSTOMER;

select CUSTOMER, PRODUCT, min(MONTH) as MONTH
from View_Final
group by CUSTOMER, PRODUCT
order by CUSTOMER;