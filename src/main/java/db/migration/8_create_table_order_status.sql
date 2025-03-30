create type status as enum ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'SERVED');

create table order_status(
    status status,
    date_order_status timestamp,
    reference_order int,
    constraint fk_order foreign key (reference_order) references "order" (reference_order)
);

