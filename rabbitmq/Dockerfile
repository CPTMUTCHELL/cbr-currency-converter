FROM rabbitmq:3.10-management-alpine

# https://stackoverflow.com/questions/30747469/how-to-add-initial-users-when-starting-a-rabbitmq-docker-container
ENV RABBITMQ_PID_FILE /var/lib/rabbitmq/mnesia/rabbitmq

COPY init.sh /init.sh
RUN chmod +x init.sh
CMD ["./init.sh"]