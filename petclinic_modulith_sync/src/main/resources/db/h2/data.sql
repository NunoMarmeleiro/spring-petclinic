INSERT INTO vets VALUES (default, 'James', 'Carter');
INSERT INTO vets VALUES (default, 'Helen', 'Leary');
INSERT INTO vets VALUES (default, 'Linda', 'Douglas');
INSERT INTO vets VALUES (default, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (default, 'Henry', 'Stevens');
INSERT INTO vets VALUES (default, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (default, 'radiology');
INSERT INTO specialties VALUES (default, 'surgery');
INSERT INTO specialties VALUES (default, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (default, 'cat');
INSERT INTO types VALUES (default, 'dog');
INSERT INTO types VALUES (default, 'lizard');
INSERT INTO types VALUES (default, 'snake');
INSERT INTO types VALUES (default, 'bird');
INSERT INTO types VALUES (default, 'hamster');

INSERT INTO owners VALUES (default, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO owners VALUES (default, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO owners VALUES (default, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO owners VALUES (default, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO owners VALUES (default, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO owners VALUES (default, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO owners VALUES (default, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO owners VALUES (default, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO owners VALUES (default, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO owners VALUES (default, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO pets VALUES (default, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets VALUES (default, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets VALUES (default, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets VALUES (default, 'Jewel', '2010-03-07', 2, 4);
INSERT INTO pets VALUES (default, 'George', '2010-01-20', 4, 5);
INSERT INTO pets VALUES (default, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets VALUES (default, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets VALUES (default, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets VALUES (default, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets VALUES (default, 'Lucky', '2010-06-24', 2, 10);

INSERT INTO visits VALUES (default, 1, '2024-08-01', 'rabies shot');
INSERT INTO visits VALUES (default, 1, '2025-10-15', 'neutered');
INSERT INTO visits VALUES (default, 2, '2024-11-09', 'rabies shot');
INSERT INTO visits VALUES (default, 2, '2025-01-05', 'neutered');
INSERT INTO visits VALUES (default, 3, '2024-12-01', 'rabies shot');
INSERT INTO visits VALUES (default, 3, '2025-02-15', 'neutered');
INSERT INTO visits VALUES (default, 4, '2024-06-01', 'rabies shot');
INSERT INTO visits VALUES (default, 4, '2025-03-24', 'neutered');
INSERT INTO visits VALUES (default, 5, '2024-08-01', 'rabies shot');
INSERT INTO visits VALUES (default, 5, '2025-01-30', 'neutered');
INSERT INTO visits VALUES (default, 6, '2024-09-01', 'rabies shot');
INSERT INTO visits VALUES (default, 6, '2025-05-03', 'neutered');
INSERT INTO visits VALUES (default, 7, '2024-10-01', 'rabies shot');
INSERT INTO visits VALUES (default, 7, '2025-09-25', 'neutered');
INSERT INTO visits VALUES (default, 8, '2024-11-02', 'rabies shot');
INSERT INTO visits VALUES (default, 8, '2025-04-06', 'neutered');
INSERT INTO visits VALUES (default, 9, '2024-07-03', 'neutered');
INSERT INTO visits VALUES (default, 9, '2025-06-19', 'neutered');
INSERT INTO visits VALUES (default, 10, '2024-07-04', 'spayed');
INSERT INTO visits VALUES (default, 10, '2025-02-07', 'spayed');
