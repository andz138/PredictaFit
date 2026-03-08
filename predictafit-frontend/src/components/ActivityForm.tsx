import { Box, Button, FormControl, InputLabel, MenuItem, Select, TextField } from "@mui/material"
import { useState } from "react"
import { addActivity } from "../services/api"

type ActivityFormProps = {
  onActivityAdded: () => void
}

type ActivityFormData = {
  type: string
  duration: string
  caloriesBurned: string
  additionalMetrics: Record<string, unknown>
}

const ActivityForm = ({ onActivityAdded }: ActivityFormProps) => {
  const [activity, setActivity] = useState<ActivityFormData>({
    type: "RUNNING",
    duration: "",
    caloriesBurned: "",
    additionalMetrics: {}
  })

  const handleSubmit = async (event: React.SyntheticEvent<HTMLFormElement>) => {
    event.preventDefault()

    try {
      await addActivity({
        activityType: activity.type,
        durationMinutes: activity.duration ? Number(activity.duration) : 0,
        caloriesBurned: activity.caloriesBurned
          ? Number(activity.caloriesBurned)
          : 0,
        startedAt: new Date().toISOString(),
        metrics: activity.additionalMetrics
      })

      onActivityAdded()

      setActivity({
        type: "RUNNING",
        duration: "",
        caloriesBurned: "",
        additionalMetrics: {}
      })
    } catch (error) {
      console.error("Failed to add activity:", error)
    }
  }

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ mb: 4 }}>
      <FormControl fullWidth sx={{ mb: 2 }}>
        <InputLabel>Activity Type</InputLabel>
        <Select
          value={activity.type}
          onChange={(event) =>
            setActivity({ ...activity, type: event.target.value as string })
          }
        >
          <MenuItem value="RUNNING">Running</MenuItem>
          <MenuItem value="WALKING">Walking</MenuItem>
          <MenuItem value="CYCLING">Cycling</MenuItem>
        </Select>
      </FormControl>

      <TextField
        fullWidth
        label="Duration (Minutes)"
        type="number"
        sx={{ mb: 2 }}
        value={activity.duration}
        onChange={(event) =>
          setActivity({ ...activity, duration: event.target.value })
        }
      />

      <TextField
        fullWidth
        label="Calories Burned"
        type="number"
        sx={{ mb: 2 }}
        value={activity.caloriesBurned}
        onChange={(event) =>
          setActivity({ ...activity, caloriesBurned: event.target.value })
        }
      />

      <Button type="submit" variant="contained">
        Add Activity
      </Button>
    </Box>
  )
}

export default ActivityForm