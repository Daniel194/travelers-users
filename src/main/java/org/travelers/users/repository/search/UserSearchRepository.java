package org.travelers.users.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.travelers.users.domain.User;

@Repository
public interface UserSearchRepository extends ElasticsearchRepository<User, String> {

    void deleteByLogin(String login);

}

