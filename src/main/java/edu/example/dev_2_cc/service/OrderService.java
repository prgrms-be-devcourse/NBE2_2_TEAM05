package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;




}

