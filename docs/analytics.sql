-- Total spent by user
-- +++++++++++++++++++

SELECT
	user_id,
	COUNT(*) AS paid_orders_count,
	SUM(price) AS total_spent_geocredits
FROM
	orders
WHERE
	status = 'PAID'
GROUP BY user_id
ORDER BY total_spent_geocredits DESC;

-- Total amount of orders by type
-- ++++++++++++++++++++++++++++++

SELECT
	type,
	COUNT(type) AS count
FROM
	orders
WHERE
	status = 'PAID'
GROUP BY type
ORDER BY count DESC;

-- Total amount of paid orders by day
-- ++++++++++++++++++++++++++++++++++

SELECT
	DATE_TRUNC('day', created_at) as order_date,
    COUNT(*) as orders_per_day,
    SUM(price) as sum_per_day
FROM
	orders
WHERE 
	status = 'PAID'
GROUP BY DATE_TRUNC('day', created_at)
ORDER BY order_date ASC;
