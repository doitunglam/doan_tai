import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRoomHotel } from 'app/shared/model/room-hotel.model';
import { getEntities as getRoomHotels } from 'app/entities/room-hotel/room-hotel.reducer';
import { getEntity, updateEntity, createEntity, reset } from './price-room.reducer';
import { IPriceRoom } from 'app/shared/model/price-room.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PriceRoomUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const roomHotels = useAppSelector(state => state.roomHotel.entities);
  const priceRoomEntity = useAppSelector(state => state.priceRoom.entity);
  const loading = useAppSelector(state => state.priceRoom.loading);
  const updating = useAppSelector(state => state.priceRoom.updating);
  const updateSuccess = useAppSelector(state => state.priceRoom.updateSuccess);
  const handleClose = () => {
    props.history.push('/price-room');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRoomHotels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...priceRoomEntity,
      ...values,
      roomHotel: roomHotels.find(it => it.id.toString() === values.roomHotel.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...priceRoomEntity,
          roomHotel: priceRoomEntity?.roomHotel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="doantaiApp.priceRoom.home.createOrEditLabel" data-cy="PriceRoomCreateUpdateHeading">
            Create or edit a PriceRoom
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="price-room-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Price" id="price-room-price" name="price" data-cy="price" type="text" />
              <ValidatedField label="Unit" id="price-room-unit" name="unit" data-cy="unit" type="text" />
              <ValidatedField id="price-room-roomHotel" name="roomHotel" data-cy="roomHotel" label="Room Hotel" type="select">
                <option value="" key="0" />
                {roomHotels
                  ? roomHotels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/price-room" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PriceRoomUpdate;
