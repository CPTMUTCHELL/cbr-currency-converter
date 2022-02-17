FROM rabbitmq:3.8-management-alpine

COPY rabbitmq.config /etc/rabbitmq
COPY definitions.json /etc/rabbitmq

RUN chown rabbitmq:rabbitmq /etc/rabbitmq/rabbitmq.config /etc/rabbitmq/definitions.json
RUN rabbitmqctl delete_user guest
CMD ["rabbitmq-server"]