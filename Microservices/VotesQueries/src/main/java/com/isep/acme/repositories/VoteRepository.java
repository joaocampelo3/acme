package com.isep.acme.repositories;

import com.isep.acme.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends CrudRepository<Vote, UUID> {

    @Query("SELECT v FROM Vote v WHERE v.voteUuid=:voteUuid")
    Optional<Vote> findByID(UUID voteUuid);

    //Delete the vote when given the voteID
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.voteUuid=:voteUuid")
    void deleteByVoteID(@Param("voteUuid") UUID voteUuid);

    //Update the vote when given the voteID
    @Transactional
    @Modifying
    @Query("UPDATE Vote v SET v.voteUuid = :voteUuid WHERE v.voteUuid=:voteUuid")
    Vote updateByVoteID(@Param("voteUuid") UUID voteUuid);

    @Query("SELECT v FROM Vote v WHERE v.voteUuid=:voteUuid")
    Optional<Vote> findById(UUID voteUuid);


}
