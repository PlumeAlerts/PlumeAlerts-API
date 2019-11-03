SELECT *
FROM notification
         INNER JOIN (SELECT * FROM twitch_subscriptions_gift) AS gift_subscription ON gift_subscription.notification_id = notification.id
WHERE channel_id = ?
  AND type = ANY(?)
ORDER BY created_at DESC
LIMIT 15;