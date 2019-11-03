SELECT *
FROM notification
         INNER JOIN (SELECT * FROM twitch_subscriptions) AS subscription ON subscription.notification_id = notification.id
WHERE channel_id = ?
  AND type = ANY(?)
ORDER BY created_at DESC
LIMIT 15;