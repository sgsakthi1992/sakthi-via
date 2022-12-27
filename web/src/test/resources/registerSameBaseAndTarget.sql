insert into rates_register(registration_id, employee_id, base)
values (1, 1, 'HUF');
insert into rates_register(registration_id, employee_id, base)
values (2, 2, 'HUF');

insert into rates_register_target(rates_register_registration_id, target)
values (1, 'INR');
insert into rates_register_target(rates_register_registration_id, target)
values (1, 'EUR');
insert into rates_register_target(rates_register_registration_id, target)
values (2, 'INR');
insert into rates_register_target(rates_register_registration_id, target)
values (2, 'EUR');