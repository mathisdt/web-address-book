INSERT INTO `family` (`id`, `last_name`, `street`, `postal_code`, `city`, `contact1`, `contact2`, `contact3`, `remarks`, `last_update`) VALUES
(1, 'Family 1', 'Street 1', '123', 'City 1', '0123-45678', '', NULL, NULL, '2014-07-02 11:52:59'),
(2, 'Family 2', 'Street 2', '123', 'City 2', '0234-56789', '', NULL, NULL, '2012-07-04 10:49:43'),
(3, 'Family 3', 'Street 3', '123', 'City 3', '0345-67890', '', NULL, NULL, '2017-02-23 08:00:49'),
(4, 'Family 4', 'Street 4', '123', 'City 4', '0456-78901', '', NULL, NULL, '2017-01-24 09:53:56');

INSERT INTO `person` (`id`, `first_name`, `last_name`, `birthday`, `contact1`, `contact2`, `contact3`, `remarks`, `family_id`, `last_update`, `ordering`) VALUES
(1, 'Surname 1-1', NULL, '01.01.', '', 'one1@example.com', NULL, NULL, 1, '2014-07-02 11:52:59', 0),
(2, 'Surname 1-2', NULL, '01.02.', '', 'one2@example.com', NULL, NULL, 1, '2014-07-02 11:52:59', 0),
(3, 'Surname 1-3', NULL, '01.03.', '', 'one3@example.com', NULL, NULL, 1, '2014-07-02 11:52:59', 0),
(4, 'Surname 1-4', NULL, '01.04.', '', 'one4@example.com', NULL, NULL, 1, '2014-07-02 11:52:59', 0),
(5, 'Surname 2', NULL, '02.01.', '', 'two@example.com', NULL, NULL, 2, '2010-10-11 21:54:10', 0),
(6, 'Surname 3-1', NULL, '03.01.', '', 'three1@example.com', NULL, NULL, 3, '2017-02-23 08:00:41', 0),
(7, 'Surname 3-2', NULL, '03.02.', '', 'three2@example.com', NULL, NULL, 3, '2017-02-23 08:00:41', 0),
(8, 'Surname 3-3', NULL, '03.03.', '', 'three3@example.com', NULL, NULL, 3, '2017-02-23 08:00:41', 0),
(9, 'Surname 3-4', NULL, '03.04.', '', 'three4@example.com', NULL, NULL, 3, '2017-02-23 08:00:41', 0),
(10, 'Surname 3-5', NULL, '03.05.', '', 'three5@example.com', NULL, NULL, 3, '2017-02-23 08:00:41', 0),
(11, 'Surname 4-1', NULL, '04.01.', '', '', NULL, NULL, 4, '2010-10-11 21:54:10', 0),
(12, 'Surname 4-2', NULL, '04.02.', '', '', NULL, NULL, 4, '2010-10-11 21:54:10', 0);
