package br.com.one.repositories;

import br.com.one.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // List<Subscription> findByStatus(SubscriptionStatus status);
}