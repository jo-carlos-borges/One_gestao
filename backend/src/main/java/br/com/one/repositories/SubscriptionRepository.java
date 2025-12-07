package br.com.one.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.one.domain.Subscription;
import br.com.one.domain.SubscriptionStatus;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

     List<Subscription> findByStatus(SubscriptionStatus status);
}