#!/bin/sh


( rabbitmqctl wait --timeout 30 $RABBITMQ_PID_FILE ; \

rabbitmqctl add_user $RABBITMQ_USER $RABBITMQ_PASSWORD 2>/dev/null ; \
rabbitmqctl set_user_tags $RABBITMQ_USER administrator ; \
rabbitmqctl set_permissions -p / $RABBITMQ_USER  ".*" ".*" ".*" ; \
echo "*** User '$RABBITMQ_USER' with password '$RABBITMQ_PASSWORD' completed. ***" ; \
rabbitmqctl delete_user guest ; \

rabbitmqctl add_vhost $RABBITMQ_VHOST ; \
rabbitmqctl set_permissions -p $RABBITMQ_VHOST $RABBITMQ_USER ".*" ".*" ".*" ; \
rabbitmqadmin -u $RABBITMQ_USER -p $RABBITMQ_PASSWORD -V $RABBITMQ_VHOST declare exchange name=$RABBITMQ_EXCHANGE type=topic ; \
rabbitmqadmin -u $RABBITMQ_USER -p $RABBITMQ_PASSWORD -V $RABBITMQ_VHOST declare queue name=$RABBITMQ_QUEUE durable=true ; \
rabbitmqadmin -u $RABBITMQ_USER -p $RABBITMQ_PASSWORD -V $RABBITMQ_VHOST declare binding source=$RABBITMQ_EXCHANGE destination=$RABBITMQ_QUEUE destination_type=queue routing_key=$RABBITMQ_BINDING_KEY  ) &

rabbitmq-server