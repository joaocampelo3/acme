package com.isep.acme.repositories;

import com.isep.acme.model.VoteTemp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface VoteTempRepository extends CrudRepository<VoteTemp, Long> {

    @Query("SELECT v FROM VoteTemp v WHERE v.voteTempUuid=:voteTempUuid")
    Optional<VoteTemp> findByID(UUID voteTempUuid);

    @Transactional
    @Modifying
    @Query("DELETE FROM VoteTemp v WHERE v.voteTempUuid=:voteTempUuid")
    void deleteByVoteID(@Param("voteTempUuid") UUID voteTempUuid);

    //Update the vote when given the voteID
    @Transactional
    @Modifying
    @Query("UPDATE VoteTemp v SET v.vote = :vote WHERE v.voteTempUuid=:voteTempUuid")
    VoteTemp updateByVoteID(@Param("voteTempUuid") UUID voteTempID, @Param("vote") String vote);

    @Query("SELECT v FROM VoteTemp v WHERE v.voteTempUuid=:voteTempUuid")
    Optional<VoteTemp> findById(UUID voteTempUuid);

}
