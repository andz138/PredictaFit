import { Box } from '@mui/material';

import ActivityForm from '../components/ActivityForm';
import ActivityList from '../components/ActivityList';

const ActivitiesPage = () => {

  return (
    <Box sx={{ p: 2 }}>

      <ActivityForm
        onActivityAdded={() => window.location.reload()}
      />

      <ActivityList />

    </Box>
  )
}

export default ActivitiesPage