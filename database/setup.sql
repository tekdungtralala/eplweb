CREATE TABLE IF NOT EXISTS `team` (
  `id` int(10) NOT NULL,
  `name` varchar(64) NOT NULL,
  `simple_name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `eplweb`.`team` (`id`, `name`, `simple_name`) 
VALUES ('20', 'Chelsea', 'chelsea'),
('19', 'Manchester City', 'mancity'),
('18', 'Southampton', 'southampton'),
('17', 'Manchester United', 'manutd'),
('16', 'Tottenham Hotspur', 'tottenham'),
('15', 'Arsenal', 'arsenal'),
('14', 'Liverpool', 'liverpool'),
('13', 'West Ham United', 'westham'),
('12', 'Swansea City', 'swansea'),
('11', 'Stoke City', 'strokecity'),
('10', 'Newcastle United', 'newcastle'),
('9', 'Crystal Palace', 'crystalpalace'),
('8', 'Everton', 'everton'),
('7', 'Aston Villa', 'astonvilla'),
('6', 'West Bromwich Albion', 'westbromwich'),
('5', 'Sunderland', 'sunderland'),
('4', 'Burnley', 'burnley'),
('3', 'Hull City', 'hullcity'),
('2', 'Queens Park Rangers', 'qpr'),
('1', 'Leicester City', 'leicester');

CREATE TABLE IF NOT EXISTS `rank` (
  `id` int(2) NOT NULL,
  `team_id` int(10) NOT NULL,
  `games_played` int(2) NOT NULL,
  `games_won` int(2) NOT NULL,
  `games_drawn` int(2) NOT NULL,
  `games_lost` int(2) NOT NULL,
  `goals_scored` int(2) NOT NULL,
  `goals_against` int(2) NOT NULL,
  `goals_difference` int(2) NOT NULL,
  `points` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `rank`
  ADD CONSTRAINT `rank_fk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;


INSERT INTO `eplweb`.`rank`(`id`, `team_id`, `games_played`, `games_won`, `games_drawn`, `games_lost`, `goals_scored`, `goals_against`, `goals_difference`, `points`) 
VALUES 
('1', '20', '22', '16', '4', '2', '51', '19', '32', '52'),
('2', '19', '22', '13', '3', '6', '37', '16', '21', '42'),
('3', '18', '22', '13', '3', '6', '37', '16', '21', '42'),
('4', '17', '22', '11', '7', '4', '36', '21', '15', '40'),
('5', '16', '22', '11', '4', '7', '32', '30', '2', '37'),
('6', '15', '21', '10', '6', '5', '37', '25', '12', '36'),
('7', '13', '22', '10', '6', '6', '35', '25', '10', '36'),
('8', '14', '22', '10', '5', '7', '31', '27', '4', '35'),
('9', '12', '22', '8', '6', '8', '26', '30', '-4', '30'),
('10', '11', '22', '8', '5', '9', '23', '27', '-4', '29'),
('11', '10', '22', '7', '6', '9', '26', '35', '-9', '27'),
('12', '9', '22', '5', '8', '9', '25', '33', '-8', '23'),
('13', '8', '21', '5', '7', '9', '30', '34', '-4', '22'),
('14', '7', '22', '5', '7', '10', '11', '25', '-14', '22'),
('15', '6', '21', '5', '6', '10', '20', '29', '-9', '21'),
('16', '5', '22', '3', '11', '8', '19', '33', '-14', '20'),
('17', '4', '22', '4', '8', '10', '21', '36', '-15', '20'),
('18', '3', '22', '4', '7', '11', '20', '30', '-10', '19'),
('19', '2', '22', '5', '4', '13', '23', '39', '-16', '19'),
('20', '1', '22', '4', '5', '13', '20', '34', '-14', '17');
