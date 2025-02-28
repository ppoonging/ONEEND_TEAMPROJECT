package com.springboot.biz.tour.mjtour;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Integer>  {
}
